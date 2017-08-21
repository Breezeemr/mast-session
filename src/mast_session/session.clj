(ns mast-session.session
  (:require [clj-time.core :as t]))

(defonce mast-session (atom {}))

(def recent-threshold (t/minutes 3))

(defn recent?
  [t1 t2 threshold]
  (or (t/within? (t/interval t1 (t/plus t1 threshold)) t2)
    (t/within? (t/interval t2 (t/plus t2 threshold)) t1)))

(defn can-add-user?
  [user]
  (recent? (:auth-time user) (t/now) recent-threshold))

(defn mark-user-access
  [user]
  (assoc user :last-access (t/now)))

;; todo what should this return when you can't add the user?
(defn add-user
  [user session]
  (if (can-add-user? user)
    (swap! session #(assoc % (:user-id user) (mark-user-access user)))
    session))

(defn update-user-time
  [user session]
  (if-let [user-session (get @session (:user-id user))]
    (swap! session #(update % (:user-id user) (mark-user-access user-session))))
  session)

(defn has-session?
  [user session]
  (if-let [user-session (get @session (:user-id user))]
    (and (recent? (t/now) (:issued-at user) recent-threshold)
      (recent? (t/now) (:last-access user-session) recent-threshold))
    false))
