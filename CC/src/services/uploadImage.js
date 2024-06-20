const { Storage } = require('@google-cloud/storage');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

const storage = new Storage({
  keyFilename: path.join(__dirname, 'capstone-project-425412-d1c7093550b5.json'),
});

const bucketName = 'aquamate-backend';


async function uploadImage(file) {
    try {
      const { path: tempFilePath, filename } = file;
  
      // Validasi file input
      if (!tempFilePath || !filename) {
        throw new Error('Invalid file input in uploadImage');
      }
  
      const destination = `images/${Date.now()}_${filename}`;
      await storage.bucket(bucketName).upload(tempFilePath, {
        destination,
      });
  
      // Make the file publicly accessible
  
      // Return the public URL
      return `https://storage.googleapis.com/${bucketName}/${destination}`;
    } catch (error) {
      console.error('Error uploading image:', error);
      throw error; // Rethrow the error to be caught in predictModel
    }
  }
  
  module.exports = uploadImage;