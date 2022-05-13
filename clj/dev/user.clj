(ns user
  (:require [ring.adapter.jetty :as jetty]
            [scramble.core :refer [handler]]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [integrant.core :as ig]
            [clojure.pprint :refer [pprint]]
            [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [clojure.tools.namespace.repl :refer [set-refresh-dirs]]))

(def config
  (-> (slurp "resources/config.edn")
      ig/read-string))

(ig/load-namespaces config)

(integrant.repl/set-prep!
  (constantly config))

(set-refresh-dirs "src")

(comment
  (prep)
  (init)
  (go)
  (halt)

  (-> (http/post "http://localhost:8080"
                 {:body (json/generate-string {:str1 "hej"
                                               :str2 "hej"})})
      :body))
