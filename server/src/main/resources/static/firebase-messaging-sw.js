importScripts("https://www.gstatic.com/firebasejs/9.1.3/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/9.1.3/firebase-messaging-compat.js");
// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
firebase.initializeApp({
  apiKey: "AIzaSyA6izrnR3iS_AcILOyR7pAT559Zs7i7QwU",
  authDomain: "newsapp-81cdd.firebaseapp.com",
  databaseURL: "https://newsapp-81cdd-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "newsapp-81cdd",
  storageBucket: "newsapp-81cdd.appspot.com",
  messagingSenderId: "268628348894",
  appId: "1:268628348894:web:9765127205dacb2bf2974f",
  measurementId: "G-6HP0Y556DW"
});

// Initialize Firebase

const messsaging = firebase.messaging()
// const auth = getAuth(messsaging)