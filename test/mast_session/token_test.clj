(ns mast-session.token-test
  (:require [clojure.test :refer :all]
            [mast-session.token :refer :all]
            [mast-session.certs :refer [google-certs]]
            [buddy.core.keys :as ks]))

(def tkn "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YjBmODA4MmE3YmUyZGIxNTAyNjhhYzZiNWNjODJhMjliZWRkMDgifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYnJlZXplZWhyLmNvbTpicmVlemUtZWhyIiwiYXVkIjoiYnJlZXplZWhyLmNvbTpicmVlemUtZWhyIiwiYXV0aF90aW1lIjoxNTAzMDg2NzQ0LCJ1c2VyX2lkIjoiU3BndXNhNThyWFN4QWNmTDBkYWI1eUJQM0l6MiIsInN1YiI6IlNwZ3VzYTU4clhTeEFjZkwwZGFiNXlCUDNJejIiLCJpYXQiOjE1MDMwODY3NDQsImV4cCI6MTUwMzA5MDM0NCwiZW1haWwiOiJkc3V0dG9uQGJyZWV6ZWVoci5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiZHN1dHRvbkBicmVlemVlaHIuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.RGNOamxM_l_OLw2xvSCyjz1ra4dBFLSYj-fxcEFL_KBHn-Ur46JQi763lY28wPxtxvlxGg8PGkV4eoEF7qOKOGnweZdOt2YEELmDN7QWWAtz04_T41DRCm0nnPbhiO3VuCnDpWOiVD8NFS0fUj-LXlyRHxCegmPqadibR_ItNUeUNbPogGO8TeIHvyr9hWvMQHQbYYXUcNO0PFJZfhOq-UfdiLPYVtP6lLMDQOv30kiMOQjJGq_L6vO654eW5U1quG6cKupCmJoDtRMgVyP_F5_3QvMCKD3Enxn7jPWXGERJLApv5JZrBZEaPFz51R1aqyRFyfJafaZnb6HylsU3Tg")

(def cert "-----BEGIN CERTIFICATE-----\nMIIDHDCCAgSgAwIBAgIINQZNrvqfzWUwDQYJKoZIhvcNAQEFBQAwMTEvMC0GA1UE\nAxMmc2VjdXJldG9rZW4uc3lzdGVtLmdzZXJ2aWNlYWNjb3VudC5jb20wHhcNMTcw\nODIxMDA0NTI2WhcNMTcwODI0MDExNTI2WjAxMS8wLQYDVQQDEyZzZWN1cmV0b2tl\nbi5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\nggEPADCCAQoCggEBALiOCSl8rcCPdcFY6Zdd1Oicgp+fAd6WmvgDjW06umtpPKZ3\nLcAtaN70GzEkN9J5IugPjBKQlKxEyw6SlA8ByTqlXZ1rOE+0NL1GttdkjW3E+3Aw\nWDJLNLggiO+5+6GyC3/HMxHzJuqOJlsaddoxxtVbG01sbjukO6YDcekvmlwUkl0R\nzqXDVvC1QEUQSGzbge4spvw6ItF6zAxOqTJIDyaxIeCG15Ns5AAIgrAaQFC9tql7\neOKJccuX0ReO/D4cMG2tt1fGTr9UmII/6oNm5Gko1gj21Zu1JvsIWiyDtYIcBXj7\nwKnr0BAO9/M+VZK8pDir4vmXXcWeUYrZEa4U4b0CAwEAAaM4MDYwDAYDVR0TAQH/\nBAIwADAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwIwDQYJ\nKoZIhvcNAQEFBQADggEBAJwbYp7iqYWFv4SCAK1YVNsCt7M2Y8U11U9kPkJe3FHo\nb+FyqMi14SurHwSnGWaoEcWUzaQ36K2f+RxAKuoOLFK9O6HtcLl0LP7ge84VWZL1\nJL4Mdpow6GcLly7AD93zYFLP7pHjB5EiCzGTHaCA2sCD24xcEe8Fxf3/2K1EOm91\n2qTImG4GAzp4bech4/uJH0YAsTWFb+MGnAVR+lVDgoz/DyYvvwwZTIw2o3W5I4R9\nHHcp+U99ZxfMGf9ttpn+0DihhkhqlMlVvAw4h8fcxdxJ3m9X9AqBp8Y5zKyzaCyJ\nEfN5CX5CpVjp3kNh5k6BVht2sMqgW3pV09LR1T8vOug=\n-----END CERTIFICATE-----\n")

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
          (:email (decode-google-payload tkn cert
                    (merge default-token-options testing-options)))))))

(deftest firebase->user-tests
  (testing "makes all datetimes good to go"
    (let [time-vals #{:expires :auth-time :issued-at}]
      (is (= #{org.joda.time.DateTime})
        (->> (select-keys (firebase->user token-contents)
               time-vals)
          (map class)
          set)))))
