(ns mast-session.token-test
  (:require [clojure.test :refer :all]
            [mast-session.token :refer :all]
            [mast-session.certs :refer [google-certs]]
            [buddy.core.keys :as ks]))

(def tkn "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YjBmODA4MmE3YmUyZGIxNTAyNjhhYzZiNWNjODJhMjliZWRkMDgifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYnJlZXplZWhyLmNvbTpicmVlemUtZWhyIiwiYXVkIjoiYnJlZXplZWhyLmNvbTpicmVlemUtZWhyIiwiYXV0aF90aW1lIjoxNTAzMDg2NzQ0LCJ1c2VyX2lkIjoiU3BndXNhNThyWFN4QWNmTDBkYWI1eUJQM0l6MiIsInN1YiI6IlNwZ3VzYTU4clhTeEFjZkwwZGFiNXlCUDNJejIiLCJpYXQiOjE1MDMwODY3NDQsImV4cCI6MTUwMzA5MDM0NCwiZW1haWwiOiJkc3V0dG9uQGJyZWV6ZWVoci5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiZHN1dHRvbkBicmVlemVlaHIuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.RGNOamxM_l_OLw2xvSCyjz1ra4dBFLSYj-fxcEFL_KBHn-Ur46JQi763lY28wPxtxvlxGg8PGkV4eoEF7qOKOGnweZdOt2YEELmDN7QWWAtz04_T41DRCm0nnPbhiO3VuCnDpWOiVD8NFS0fUj-LXlyRHxCegmPqadibR_ItNUeUNbPogGO8TeIHvyr9hWvMQHQbYYXUcNO0PFJZfhOq-UfdiLPYVtP6lLMDQOv30kiMOQjJGq_L6vO654eW5U1quG6cKupCmJoDtRMgVyP_F5_3QvMCKD3Enxn7jPWXGERJLApv5JZrBZEaPFz51R1aqyRFyfJafaZnb6HylsU3Tg")

(def token-contents {:firebase
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
                     :iat 1503086744})

(def cert (-> google-certs
            vals
            last
            ks/str->public-key))

(def testing-options {:leeway 1000000000000}) ;; allow for expired certs

(deftest decode-object-test
  (testing "can read firebase tokens"
    (is (= "dsutton@breezeehr.com"
          (:email (decode-object tkn (merge default-token-options testing-options)))))))

(deftest firebase->user-tests
  (testing "makes all datetimes good to go"
    (let [time-vals #{:expires :auth-time :issued-at}]
      (is (= #{org.joda.time.DateTime})
        (->> (select-keys (firebase->user token-contents)
               time-vals)
          (map class)
          set)))))
