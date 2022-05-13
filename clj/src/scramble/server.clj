(ns scramble.server
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [cheshire.core :as json]
            [ring.middleware.cors :refer [wrap-cors]]
            [scramble.core :refer [scramble?]]))

(defmethod ig/init-key ::adapter
  [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler
                   (-> opts
                       (dissoc :handler)
                       (assoc :join? false))))

(defmethod ig/halt-key! ::adapter
  [_ server] (.stop server))

(defmethod ig/init-key ::handler
  [_ _]
  (wrap-cors
    (fn [req]
      #_(prn (-> (:body req)
               clojure.java.io/reader
               (json/parse-stream #(keyword %))))
      (let [{:keys [str1 str2]} (-> (:body req)
                                    clojure.java.io/reader
                                    (json/parse-stream #(keyword %)))]

        (-> (scramble? str1 str2)
            json/generate-string
            resp/response)))

    :access-control-allow-origin [#".*"]
    :access-control-allow-methods [:get :post :option :head :delete]))

(defn -main []
  (-> (slurp "resources/config.edn")
      ig/read-string
      ig/init))