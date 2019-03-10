const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

// updates the Info/summary of expenses for a list whenever a new expense is added
exports.updateExpenseSharerInfo = functions.firestore
  .document('expense_lists/{listId}/expenses/{expenseId}')
  .onCreate((snapshot, context) => {

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

        // Update info
        return transaction.set(listRef, {
          lastFunctionsUpdateIDs: lastUpdates,
          sharerInfo: { [userId]: { avgExpenses: newAvgExpenses, numExpenses: newNumExpenses, sumExpenses: newSumExpenses } }
        }, { merge: true, });
      });
    });

  });