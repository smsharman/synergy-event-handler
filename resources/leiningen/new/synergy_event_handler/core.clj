(ns {{name}}.core
(:require [uswitch.lambada.core :refer [deflambdafn]]
  [clojure.data.json :as json]
  [clojure.java.io :as io]
  [cognitect.aws.client.api :as aws]
  [synergy-specs.events :as synspec]
  [synergy-events-stdlib.core :as stdlib]
  [clojure.spec.alpha :as s]
  [taoensso.timbre :as timbre
   :refer [log trace debug info warn error fatal report
           logf tracef debugf infof warnf errorf fatalf reportf
           spy get-env]])
(:gen-class))

;; Declare clients for AWS services required

(def sns (aws/client {:api :sns}))

(def ssm (aws/client {:api :ssm}))

;; Set this on a per-event-handler basis - this is the eventAction and eventVersion that this handler handles
(def myEventAction "")
(def myEventVersion 1)

(def snsArnPrefix (atom ""))

(def eventStoreTopic (atom ""))

;; Now define specific SNS topics to be used for this handler
;; At a minimum, best practice should have an event emitted to record success, and an event emitted to record problems
(def successQueue "")
(def failQueue "")

;; Specific handler logic here
;; The process-event function takes in a namespaced standard Synergy event and checks that it is the correct handler for
;; the event version and event type (based on eventAction). In the example below, the function simply sends messages to
;; the success and fail queues above depending on whether the version/types match or not. Update logic here according to
;; need

(defn process-event [event]
  "Process an inbound event - usually emit a success/failure message at the end"
  (if (empty? @snsArnPrefix)
    (stdlib/set-up-topic-table snsArnPrefix eventStoreTopic ssm))
  (let [validateEvent (stdlib/validate-message event)]
    (if (true? (get validateEvent :status))
        (if (and (= (get event ::synspec/eventAction) myEventAction)
                 (= (get event ::synspec/eventVersion) myEventVersion))
          (stdlib/send-to-topic successQueue event @snsArnPrefix sns "I route this!")
          (stdlib/send-to-topic failQueue event @snsArnPrefix sns "I don't route this!"))
      (stdlib/gen-status-map false "invalid-message-format" (get validateEvent :return-value)))))

;; Note, we have the handler and then the processor in order to allow for testing. Processor takes
;; namespaced event
(defn handle-event
  [event]
  (let [cevent (json/read-str (get (get (first (get event :Records)) :Sns) :Message) :key-fn keyword)
        nsevent (synergy-specs.events/wrap-std-event cevent)]
    (info "Received the raw event : " (print-str event))
    (info "Converted event " (print-str cevent))
    (info "Namespaced, converted event : " (print-str nsevent))
    (process-event nsevent)))


(deflambdafn {{name}}.core.Route
             [in out ctx]
             "Takes a JSON event in standard Synergy Event form from the Message field, convert to map and send to routing function"
             (let [event (json/read (io/reader in) :key-fn keyword)
                   res (handle-event event)]
               (with-open [w (io/writer out)]
                 (json/write res w))))
