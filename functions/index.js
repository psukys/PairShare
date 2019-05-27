const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();


// Sends a notifications to all users when a new message is posted.
exports.sendExpenseNotification = functions.runWith({ memory: "128MB", timeoutSeconds: 30 })
  .firestore.document('expense_lists/{listId}/expenses/{expenseId}')
  .onCreate(async (snapshot,context) =>{
    // Added amount.
    const amount = snapshot.data().amount;
    const comment = snapshot.data().comment;
    // User adding the expense.
    const userId = snapshot.data().userID;
    // The reference to the list the expense was added to.

    const expenseList = await db.collection('expense_lists').doc(context.params.listId).get();
    const listName = expenseList.data().listName;
    const user = await db.collection('users').doc(userId).get();

    const userName = user.data().username;
    const text =  `Added ${amount}€ to List "${listName}": ${comment}`

    const payload = {
      notification: {
        title: `${userName} added an Expense`,
        body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
//            icon: snapshot.data().profilePicUrl || '/images/profile_placeholder.png',
//            click_action: `https://${process.env.GCLOUD_PROJECT}.firebaseapp.com`,
      }
    };

    // Get all sharers involved in the list the expense was added to
    const sharers = expenseList.data().sharers;
    const results = [];
    // Get all involved user documents except the user that triggered the event.
    for (const usr of sharers) {
      if(usr!==userId) 
        results.push(db.collection('users').doc(usr).get());
    }
    const users= await Promise.all(results);
   
    // Collect the tokens of all users that should be notified
    const tokens = [];
    for (const usr of users){
      //console.log("Will notify user" + usr.data().mail);
      if(usr.data().fcmToken){
      tokens.push(usr.data().fcmToken);}
      
    }
    //console.log("tokens "+tokens);
  
    // Send out notifications
    if(tokens.length>0)
      await admin.messaging().sendToDevice(tokens, payload);
   
  });


// IMPORTANT: Function below currently not used, was part of an earlier draft for how the app updates the sharerInfo for each list once a new expense is added.
// This is now done locally on each client using the new increment property to update the parent list.

/*
// updates the Info/summary of expenses for a list whenever a new expense is added
exports.updateExpenseSharerInfo = functions.runWith({ memory: "128MB", timeoutSeconds: 30 }).firestore
  .document('expense_lists/{listId}/expenses/{expenseId}')
  .onCreate((snapshot, context) => {
    //TODO: optional enable retries if function fails in firebase console.
    const eventId = context.eventId;
    // Added amount.
    const amount = snapshot.data().amount;
    // User adding the expense.
    const userId = snapshot.data().userID;
    // The reference to the list the expense was added to.
    const listRef = db.collection('expense_lists').doc(context.params.listId);

    // Update aggregations in a transaction
    return db.runTransaction(transaction => {
      return transaction.get(listRef).then(listDoc => {

        // If the list of last updates already contains the ID the update was already processed by a function.
        // In that case simply return without any action.
        var lastUpdates = listDoc.data().lastFunctionsUpdateIDs;
        //console.log('Opened updateIDs');

        if (typeof lastUpdates === "undefined") {
          lastUpdates = [];
        }
        if (lastUpdates.includes(eventId)) {
          return null;
        }

        // Add the current eventId to the list of the last function calls.
        // Prevents the function to be executed more than once for the same event (Can happen that the function is called multiple times).
        lastUpdates.push(eventId);

        // While there are more than 20 ids in the list, remove the oldest (=first) ones.
        // This assumes that there are not that many function calls per second.
        // Issues with this approach could occur if there are lots of function calls and a 
        // ID gets removed before the function is called a second time for the same event(which can happen).
        // In such a case the update will be applied twice.
        while (lastUpdates.length > 20) {
          lastUpdates.shift();
        }

        var sharerInfo = listDoc.data().sharerInfo[userId];

        // Compute new number of expenses
        var newNumExpenses = sharerInfo.numExpenses + 1;

        // Compute new total and average expenses
        var newSumExpenses = sharerInfo.sumExpenses + amount;
        var newAvgExpenses = newSumExpenses / newNumExpenses;

        //Set the server timestamp
        var ts = admin.firestore.FieldValue.serverTimestamp();
        // Update info
        return transaction.set(listRef, {
          modified: ts,
          lastFunctionsUpdateIDs: lastUpdates,
          sharerInfo: { [userId]: { avgExpenses: newAvgExpenses, numExpenses: newNumExpenses, sumExpenses: newSumExpenses } }
        }, { merge: true, });
      });
    });

  });
*/