(ns mast-session.token
  (:require [buddy.sign.jwt :as jwt]
            [buddy.sign.jws :as jws]
            [buddy.core.keys :as ks]
            [clj-time.core :as t]
            [clj-time.coerce :as convert]
            [mast-session.certs :as certs]))

(defn decode-google-payload
  [token cert opts]
  (try
    (jwt/unsign token cert opts)
    (catch Exception e nil)))

(def certs (map ks/str->public-key (vals certs/google-certs)))


(def default-token-options
  {:alg :rs256})
;; this won't need to map over all certs once we can see custom keys
;; in the header. See
;; https://github.com/funcool/buddy-sign/pull/52.
(defn decode-object
  ([token]
   (decode-object token default-token-options))
  ([token options]
   (->> certs
     (map #(decode-google-payload token % options))
     (filter some?)
     first)))

;; [:firebase :email :aud :sub :iss :exp :email_verified :auth_time :user_id :iat]


;; {:firebase
;;  {:identities {:email ["dsutton@breezeehr.com"]},
;;   :sign_in_provider "password"},
;;  :email "dsutton@breezeehr.com",
;;  :aud "breezeehr.com:breeze-ehr",
;;  :sub "Spgusa58rXSxAcfL0dab5yBP3Iz2",
;;  :iss "https://securetoken.google.com/breezeehr.com:breeze-ehr",
;;  :exp 1503090344,
;;  :email_verified false,
;;  :auth_time 1503086744,
;;  :user_id "Spgusa58rXSxAcfL0dab5yBP3Iz2",
;;  :iat 1503086744}

(defn firebase->user
  "Normalize timestamps"
  [firebase]
  (letfn [(timestamp [m v] (update m v convert/from-epoch))]
    (let [bad-names    (-> firebase
                         (timestamp :exp)
                         (timestamp :auth_time)
                         (timestamp :iat))
          renamed-keys {:iat            :issued-at
                        :exp            :expires
                        :iss            :issuer
                        :sub            :subject
                        :aud            :audience
                        :auth_time      :auth-time
                        :user_id        :user-id
                        :email_verified :email-verified}]
      (merge (select-keys bad-names [:identities :email])
        (reduce (fn [m [old-name new-name]]
                  (assoc m new-name (get bad-names old-name)))
          {}
          renamed-keys)))))

(defn token->user
  [token]
  (if-let [firebase (decode-object token)]
    (firebase->user firebase)
    nil))
