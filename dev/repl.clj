(ns mast-session.repl
  (:require [ring.adapter.jetty :as jetty]
            [mast-session.handler :as handler]
            [ring.middleware.cors :refer [wrap-cors]]))

(def server (atom nil))
(comment
  (reset! server (jetty/run-jetty (wrap-cors handler/app
                                 :access-control-allow-origin [#".*"]
                                 :access-control-allow-methods [:get :post])
                {:port 3001
                 :join? false}))
  (.stop @server))





