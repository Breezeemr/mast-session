(ns mast-session.handler
  (:require [mast-session.core :as sesh]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response ]]
            [ring.util.response :refer [response]]
            [cheshire.generate :refer [add-encoder]]
            [clj-time.coerce :as convert]))

(add-encoder org.joda.time.DateTime
  (fn [dt jsonGenerator]
    (.writeString jsonGenerator (str (convert/to-epoch dt)))))

(defroutes json-routes
  (GET "/auth/:token" [token] (response (sesh/is-authorized? token)))
  (GET "/admin/sessions" [] (response @sesh/session)))

(defroutes app-routes
  (GET "/" [] "hello world")
  (route/not-found "nope"))

(defroutes all-routes
  (wrap-json-response json-routes {:pretty true})
  (wrap-defaults app-routes site-defaults))

(def app all-routes)
