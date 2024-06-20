const db = require('../db');
const { FieldValue } = require('@google-cloud/firestore');

class Answer {
  static async create(data) {
    const answerRef = db.collection('answers').doc();
    await answerRef.set({
      ...data,
      createdAt: FieldValue.serverTimestamp(),
    });
    return answerRef.id;
  }
}

module.exports = Answer;