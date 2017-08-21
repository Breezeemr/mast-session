(ns mast-session.core
  (:require [mast-session.token :as tok]
            [mast-session.session :as session]))

(def session session/mast-session)

(defn is-authorized?
  [token]
  (if-let [user (tok/token->user token)]
    (cond
      (session/has-session? user session) {:authorized true
                                           :session :existing}

      (session/can-add-user? user)
      (do (session/add-user user session)
          {:authorized true
           :session :created})

      :else
      {:authorized false})
    {:authorized false}))
