(ns foo.droid.main
  (:require [neko.resource :as res])
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui post]]
        [neko.ui :only [make-ui]]
        [neko.notify :as n :only [toast notification]]
        [neko.log :as log]
        [neko.ui.traits :only [deftrait]]
        [neko.ui.mapping :only [add-trait!]])
  (:import com.google.android.gms.common.GooglePlayServicesUtil
           com.google.android.gms.gcm.GoogleCloudMessaging
           android.view.View
           android.widget.TextView
           android.os.AsyncTask
           java.lang.Thread))

(defn check-google-play-services [context]
  (GooglePlayServicesUtil/isGooglePlayServicesAvailable context))

(def sender-id "617220181631")

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

(def ui [:linear-layout {}])

(def ui2 [:linear-layout {:id-holder true}
         [:edit-text {:text regid :text-size}]])

(defn register-in-background [context]
  (on-background
   (fn [] (register context))
   (fn [r]
     (reset! regid r))))

(defactivity foo.droid.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    ;(register-in-background this)
    (on-ui
     (set-content-view! a (make-ui ui)))))
