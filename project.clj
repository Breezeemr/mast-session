(defproject mast-session "0.1.0-SNAPSHOT"
  :description "Session for mast"
  :url "http://breezeehr.com"
  :license {:name "No License"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [buddy "1.3.0" :exclusions [buddy-sign]]
                 [buddy/buddy-sign "2.0.0"]
                 [clj-time "0.14.0"]
                 [clj-http "3.7.0"]
                 [cheshire "5.8.0"]

                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.11"]]
  :plugins [[lein-ring "0.12.0"]]
  :ring {:handler mast-session.handler/app}
  :profiles {:dev {:dependencies [[ring/ring-jetty-adapter "1.5.0"]]
                   :source-paths ["dev"]}})
