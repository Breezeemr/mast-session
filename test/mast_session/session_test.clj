(ns mast-session.session-test
  (:require [mast-session.session :refer :all]
            [clojure.test :refer [deftest is testing]]
            [clj-time.core :as t]))

(defn user-auth-time
  ([]
   (user-auth-time 1))
  ([minutes-ago]
   (user-auth-time minutes-ago {:user-id 1}))
  ([minutes-ago user]
   (merge user
     {:auth-time (t/plus (t/now) (t/minutes (- minutes-ago)))})))

(deftest can-add-user?-tests
  (testing "if recent token then yes"
    (is (can-add-user? (user-auth-time 2)))
    (is (not (can-add-user? (user-auth-time 4))))))

(deftest has-session?-tests
  (testing "when no active session"
    (let [session (atom {})
          user    (user-auth-time)]
      (is (not (has-session? user session))))
    (let [user-id 1
          user    (user-auth-time 5
                    {:user-id     user-id
                     :last-access (t/minus (t/now)
                                    recent-threshold
                                    (t/minutes 1))})
          session (atom {user-id user})]
      (is (not (has-session? user session)))))

  (testing "user present and within the time threshold"
    (let [user-id 1
          user    (user-auth-time 5
                    {:user-id     user-id
                     :last-access (t/plus
                                    (t/minus (t/now)
                                      recent-threshold)
                                    (t/minutes 1))})
          session (atom {user-id user})]
      (is (= true (has-session? user session))))))
