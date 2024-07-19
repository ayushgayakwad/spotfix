// Replace with your Firebase project configuration
const firebaseConfig = {
  apiKey: "...",
  authDomain: "...",
  projectId: "...",
  storageBucket: "...",
  messagingSenderId: "...",
  appId: "..."
};

firebase.initializeApp(firebaseConfig);

const database = firebase.database();

const reference = database.ref('your/data/path');
reference.on('value', (snapshot) => {
  const data = snapshot.val();
  // Update your dashboard with the retrieved data
  console.log(data);
});

const reference = database.ref('your/data/path/child');
reference.on('value', (snapshot) => {
  const data = snapshot.val();
  // Update your dashboard with the retrieved data
  console.log(data);
});

const reference = database.ref('your/data/path');
reference.set({
  key1: "value1",
  key2: "value2"
});

// Update specific child
reference.child('key1').update({
  value: "updatedValue"
});
