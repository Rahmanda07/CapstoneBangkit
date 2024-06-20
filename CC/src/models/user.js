const db = require('../db');
const { FieldValue } = require('@google-cloud/firestore');
const bcrypt = require('bcrypt');

class User {
  static async create(username, password) {
    const userRef = db.collection('users').doc(username);
    const hashedPassword = await bcrypt.hash(password, 10);
    await userRef.set({
      username,
      password: hashedPassword,
      createdAt: FieldValue.serverTimestamp(),
    });
  }

  static async findByUsername(username) {
    const userDoc = await db.collection('users').doc(username).get();
    if (!userDoc.exists) {
      return null;
    }
    return userDoc.data();
  }
}

module.exports = User;