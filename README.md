# Scramble
In the name of separation of concerns 
(and mostly not messing up my IDE) I've chosen
a "monorepo" style project layout.

## Backend
`cd clj`

### Starting the server
`clj -M -m scramble.server`

### Running tests
`clj -M:run/test`

## Frontend
`cd cljs`

### Starting a dev server
`clj -M:build/dev`

### Running tests
I didn't feel like it made sense
to add any tests as all "units" are I/O.
