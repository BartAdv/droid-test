(ns foo.droid.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]]
        [neko.notify :only [toast]])
  (:import com.google.android.gms.common.GooglePlayServicesUtil
           com.google.android.gms.gcm.GoogleCloudMessaging))

(defn check-google-play-services [context]
  (GooglePlayServicesUtil/isGooglePlayServicesAvailable context))

(def sender-id "617220181631")

(defn register [context]
  (let [gcm (GoogleCloudMessaging/getInstance context)]
    (.register gcm (into-array String [sender-id]))))

(defactivity foo.droid.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    (on-ui
     (set-content-view! a
      (make-ui [:linear-layout {}
                [:text-view {:text "Hello from Clojure!"}]
                [:button {:text "bash it" :on-click (fn [w] (toast "Endure. In enduring, grow strong."))}]])))))
