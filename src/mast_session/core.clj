(ns mast-session.core
  (:require [mast-session.token :as tok]
            [mast-session.session :as session]))

(def session session/mast-session)

(defn is-authorized?
  [token]
  (if-let [user (tok/decode-object token)]
    (let [user (tok/firebase->user user)]
      (cond
        (session/has-session? user session) true

        (session/can-add-user? user)
        (do (session/add-user user session)
            true)

        :else
        false))
    false))
