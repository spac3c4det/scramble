(ns scramble.server-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [integrant.core :as ig]))

(def config
  (-> (slurp "resources/config.edn")
      ig/read-string
      (assoc-in [:scramble.server/adapter :port] 8888)))

(use-fixtures :once
              (fn [a-test]
                (let [_ (ig/load-namespaces config)

                      system (ig/init config)]
                  (a-test)
                  (ig/halt! system))))

(deftest ^:integration http-request-test
  (testing "Send a request to the webserver")
  (is (= (:body (http/post (str "http://localhost:" (get-in config [:scramble.server/adapter :port]))
                           {:body (json/generate-string {:str1 "katas"
                                                         :str2 "steak"})}))
         "false")))

