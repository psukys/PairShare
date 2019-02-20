const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

// updates the Info/summary of expenses for a List whenever a new expense is added
exports.updateExpenseSharerInfo = functions.firestore
  .document('expense_lists/{listId}/expenses/{expenseId}')
  .onCreate((snapshot, context) => {

    // TODO: could be triggered multiple times or be inconsistent. Make sure this doesn't happen or doesn't matter
 
    // Added amount.
    const amount = snapshot.data().amount;
    // User adding the expense.
    const userId = snapshot.data().userID;
    // The reference to the list the expense was added to.
    const listRef = db.collection('expense_lists').doc(context.params.listId);
    
    // Update aggregations in a transaction
    return db.runTransaction(transaction => {
      return transaction.get(listRef).then(listDoc => {
        var sharerInfo = listDoc.data().sharerInfo[userId];

        // Compute new number of expenses
        var newNumExpenses = sharerInfo.numExpenses + 1;

        // Compute new total and average expenses
        var newSumExpenses = sharerInfo.sumExpenses + amount;
        var newAvgExpenses= newSumExpenses / newNumExpenses;


        // Update info
        return transaction.set(listRef, {
          sharerInfo: { [userId]: {avgExpenses: newAvgExpenses, numExpenses: newNumExpenses, sumExpenses: newSumExpenses}}
        },{merge:true,});
      });
    });

  });