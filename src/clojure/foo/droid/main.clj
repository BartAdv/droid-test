(ns foo.droid.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]]
        [neko.notify :only [toast]])
  (:import com.google.android.gms.common.GooglePlayServicesUtil
           com.google.android.gms.gcm.GoogleCloudMessaging
           android.os.AsyncTask))

(defn check-google-play-services [context]
  (GooglePlayServicesUtil/isGooglePlayServicesAvailable context))

(def sender-id "617220181631")

(def a)

(defn on-background [f s]
  (let [task (proxy [AsyncTask] []
                (doInBackground [_]
                  (f))
                (onPostExecute [res]
                  (s res)))]
    (.execute task (into-array Object []))))

(def regid (atom))

(defn register [context]
  (let [gcm (GoogleCloudMessaging/getInstance context)]
    (.register gcm (into-array String [sender-id]))))

(def ui [:linear-layout {}
         [:text-view {:text "Hello from Clojure!!!"}]
         [:button {:text "bash it"
                   :on-click (fn [w] (toast "Endure. In enduring, grow strong."))}]
         [:button {:text "try and fetch id"
                   :on-click (fn [w] (on-background
                                     (fn [] (register a))
                                     (fn [r] (on-ui (toast r)))))}]])

(defactivity foo.droid.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    (on-ui
     (set-content-view! a
      (make-ui ui)))))
