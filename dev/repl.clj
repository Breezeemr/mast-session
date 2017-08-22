(ns mast-session.repl
  (:require [ring.adapter.jetty :as jetty]
            [mast-session.handler :as handler]))

(comment
  (def server (jetty/run-jetty handler/app {:port 3001}))
  (.stop server))





