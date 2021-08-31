# synergy-event-handler

A Leiningen template for Synergy Event Handlers.

## Usage

lein new synergy-event-handler <project-name>

Generates a new project for the synergy-event-handler style. Update the core.clj namespace
with application specific logic. Creates an uberjar called:

synergy-handler-<project-name>.jar

Scripts deployFunction.sh and updateFunction.sh can be used
to deploy/update Lambda on AWS

## License

Copyright © 2020 Hackthorn Imagineering Ltd

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

