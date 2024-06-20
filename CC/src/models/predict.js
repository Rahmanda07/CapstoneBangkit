const db = require('../db');
const { FieldValue } = require('@google-cloud/firestore');

class Predict {
  static async create(data) {
    const predictRef = db.collection('predict').doc();
    await predictRef.set({
      ...data,
      createdAt: FieldValue.serverTimestamp(),
    });
    return predictRef.id;
  }

  static async getAll() {
    const predictSnapshot = await db.collection('predict').get();
    return predictSnapshot.docs.map(doc => doc.data());
  }


}

module.exports = Predict;