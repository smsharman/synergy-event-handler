(defproject {{name}} "0.1.1"
  :description "Synergy Event Handler for XX"
  :url "http://synergyxm.ai/event-handler"
  :license {:name "Hackthorn Innovation Ltd"
            :url ""}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [uswitch/lambada "0.1.2"]
                 [cheshire "5.10.1"]
                 [org.clojure/tools.reader "1.3.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [synergy-specs "0.1.9"]
                 [com.cognitect.aws/api "0.8.456"]
                 [com.cognitect.aws/endpoints "1.1.11.753"]
                 [com.cognitect.aws/sqs "770.2.568.0"]
                 [com.cognitect.aws/sns "773.2.578.0"]
                 [com.cognitect.aws/ssm "794.2.640.0"]
                 [synergy-events-stdlib "0.1.8"]]
  :repl-options {:init-ns {{name}}.core}
  :profiles {:uberjar {:aot :all}}
  :uberjar-name "synergy-handler-{{name}}.jar")
