const db = require('../db');
const { FieldValue } = require('@google-cloud/firestore');

class Question {
  static async create(data) {
    const questionRef = db.collection('questions').doc();
    await questionRef.set({
      ...data,
      createdAt: FieldValue.serverTimestamp(),
    });
    return questionRef.id;
  }

  static async getById(id) {
    try {
      const doc = await db.collection('questions').doc(id).get();
      if (!doc.exists) {
        throw new Error('No such document!');
      }
      return { id: doc.id, ...doc.data() };
    } catch (error) {
      console.error('Error getting document:', error);
      throw new Error('Unable to retrieve question entry');
    }
  }

  static async getAllWithAnswers() {
    const questionsSnapshot = await db.collection('questions').get();
    const questions = await Promise.all(
      questionsSnapshot.docs.map(async (doc) => {
        const answersSnapshot = await db.collection('answers').where('questionId', '==', doc.id).get();
        const allAnswers = answersSnapshot.docs.map(answerDoc => ({ id: answerDoc.id, ...answerDoc.data() }));
        return {
          id: doc.id,
          ...doc.data(),
          allAnswers
        };
      })
    );
    return questions;
  }

  static async delete(id) {
    const batch = db.batch();

    // Delete all answers related to the question
    const answersSnapshot = await db.collection('answers').where('questionId', '==', id).get();
    answersSnapshot.forEach(doc => {
      batch.delete(doc.ref);
    });

    // Delete the question itself
    const questionRef = db.collection('questions').doc(id);
    batch.delete(questionRef);

    await batch.commit();
  }
}

module.exports = Question;