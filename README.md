# mast-session

A Clojure library designed to ... well, that part is up to you.

## Usage

This will interact with firebase jwt at one edge and will be largely difficult to test in that regard. Tokens expire and must be signed with a private key.

These keys can be generated using cljsjs/firebase "4.2.0" and the config info from the Firebase Setup https://console.firebase.google.com/project/breezeehr.com:breeze-ehr/authentication/users  and clicking on "Web Setup".

```clojure

(defn register-firebase [config]
  (.initializeApp js/firebase config))

;; hook up app onces to firebase
(defonce registered-with-firebase
  (do (register-firebase firebase-config)
      true))

(defn authenticate [username password]
  (let [c (async/chan)]
    (-> (.auth js/firebase)
      (.setPersistence persistence)
      (.then (fn []
               (.signInWithEmailAndPassword (.auth js/firebase)  username password)))
      (.then
        (fn [result]
          (js/console.log "worked:" result "worked")
          (go (async/>! c result)))
        (fn [err]
          (let [e (which-error err)]
            (js/console.log e)
            (go (async/>! c e))))))
    c))

(let [chan         (authenticate "dsutton@breezeehr.com" "easypassword")
      forceRefresh false]

    (go
      (let [user (async/<! chan identity) ]
        (-> (.getIdToken user forceRefresh)
          (.then
            (fn [token]
              (js/console.log token))
            (fn [err] err))))))

```

This will print a token which can be decoded from `mast-session.core/decode-object` into something like the following:

```clojure

{:firebase
 {:identities {:email ["dsutton@breezeehr.com"]},
  :sign_in_provider "password"},
 :email "dsutton@breezeehr.com",
 :aud "breezeehr.com:breeze-ehr",
 :sub "Spgusa58rXSxAcfL0dab5yBP3Iz2",
 :iss "https://securetoken.google.com/breezeehr.com:breeze-ehr",
 :exp 1503090344,
 :email_verified false,
 :auth_time 1503086744,
 :user_id "Spgusa58rXSxAcfL0dab5yBP3Iz2",
 :iat 1503086744}

```
## License

Copyright © 2017 BreezeEHR
