(ns scramble.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn render! [a-str]
  (-> (.getElementById js/document "scramble?-out")
      (.-innerHTML)
      (set! a-str)))

(defn scramble?! [str1 str2]
  (go (let [response (<! (http/post "http://localhost:8080"
                                    {:headers           {"Access-Control-Allow-Origin" "*"}
                                     :json-params       {:str1 str1
                                                         :str2 str2}
                                     :with-credentials? false}))]
        (when (= (:status response) 200)
          (render! (str "-> " (:body response)))))))

(defn get-input [elem-id]
  (-> (.getElementById js/document elem-id)
      (.-value)))

(.addEventListener (.getElementById js/document "submit")
                   "click"
                   #(scramble?! (get-input "str1")
                                (get-input "str2")))
