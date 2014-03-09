(ns foo.droid.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui post]]
        [neko.ui :only [make-ui]]
        [neko.notify :only [toast]]
        [neko.log :as log])
  (:import com.google.android.gms.common.GooglePlayServicesUtil
           com.google.android.gms.gcm.GoogleCloudMessaging
           android.os.AsyncTask
           java.lang.Thread))

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

(def regid (atom nil))

(defn register [context]
  (let [gcm (GoogleCloudMessaging/getInstance context)]
    (.register gcm (into-array String [sender-id]))))

(def ui [:linear-layout {}
         [:text-view {:text "Hello from Clojure!!!"}]
         [:button {:text "bash it"
                   :on-click (fn [w]
                               (let [tb (.findViewById a 1)]
                                 (.setText tb @regid)))}]
         [:edit-text {:id 1}]])

(defn register-in-background []
  (on-background
   (fn [] (register a))
   (fn [r]
     (reset! regid r))))

(defactivity foo.droid.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    (register-in-background)
    (on-ui
     (set-content-view! a
      (make-ui ui)))))
