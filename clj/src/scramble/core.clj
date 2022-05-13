(ns scramble.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [cheshire.core :as json]
            [ring.middleware.cors :refer [wrap-cors]]))

;; --- scramble function
(defn index-str
  "Recursively builds a hashmap associating
   the chars in str with the number occurrences.
   Example: hey -> {\\ h 1
                    \\ e 1
                    \\ y 1}"
  ([str] (index-str str {}))
  ([str indexed-str]
   (if (nil? (first str))
     indexed-str
     (index-str (rest str)
                (update indexed-str (first str)
                        (fn [cnt]
                          (if (nil? cnt)
                            1
                            (inc cnt))))))))

(defn reverse-index
  "Looks up the characters in str1 in "
  [str1 indexed-str]
  (if (nil? (first str1))                                   ; check if last char in string
    true
    (if (nil? (get indexed-str (first str1)))               ; check if char exist in index
      false
      (reverse-index (rest str1)
                     (update indexed-str (first str1)
                             (fn [cnt]
                               (if (= cnt 0)
                                 nil
                                 (dec cnt))))))))

(defn scramble? [str1 str2]
  (reverse-index str2 (index-str str1)))


;; --- web server
(def handler
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

(defn start!
  "Starts the server, blocking the main thread"
  ([] (start! {}))
  ([{:keys [port join?]
     :or   {port 8080, join? false}
     :as   opts}]
   (jetty/run-jetty handler opts)))


(comment
  (defmethod ig/init-key :adapter/jetty
    [_ {:keys [handler] :as opts}]
    (jetty/run-jetty handler
                     (-> opts
                         (dissoc :handler)
                         (assoc :join? false))))

  (defmethod ig/halt-key! :adapter/jetty
    [_ server] (.stop server))

  (defmethod ig/init-key :handler/scramble?
    [_ _]
    (wrap-cors
      (fn [req]
        (prn (-> (:body req)
                 clojure.java.io/reader
                 (json/parse-stream #(keyword %))))
        (let [{:keys [str1 str2]} (-> (:body req)
                                      clojure.java.io/reader
                                      (json/parse-stream #(keyword %)))
              _ (prn str1 str2)]

          (-> (scramble? str1 str2)
              json/generate-string
              resp/response)))
      :access-control-allow-origin [#".*"]
      :access-control-allow-methods [:get :post :option :head :delete])))