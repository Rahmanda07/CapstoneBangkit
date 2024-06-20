const predictClassification = require('../services/inferenceServices');
const predictDB = require('../models/predict');
const uploadImage = require('../services/uploadImage');
const Joi = require('joi');
const fs = require('fs').promises;

const predictRoutes = [
  {
    method: 'POST',
    path: '/aquamate/predict',
    options: {
      payload: {
        parse: true,
        output: 'file',
        allow: 'multipart/form-data',
        maxBytes: 2 * 1024 * 1024, // 2MB limit
        multipart: true,
      },
      validate: {
        payload: Joi.object({
          image: Joi.any().required(),
        }),
      },
    },
    handler: async (request, h) => {
      try {
        const  { image }  = request.payload;
        const { model } = request.server.app;

        const imageUrl = await uploadImage(image);

        const imageBuffer = await fs.readFile(image.path);

        const {label, explanation} = await predictClassification(model, imageBuffer);

        if (!label || !explanation) {
        return h.response({
         status: 'fail',
         message: 'Result or explanation is missing'
        }).code(400);
        }
 

        const id = await predictDB.create({
          predictUrl: imageUrl,
          result: label,
          explanation: explanation,
        });

        return h.response({
          status: true,
          message: "added successfully",
          id: id,
          predicUrl: imageUrl,
          result: label,
          explanation: explanation,
        }).code(201);
      } catch (e) {
        console.error(e);
        return h.response({
          status: false,
          message: "Error while adding prediction",
        }).code(500);
      }
    },
  },
  {
    method: 'GET',
    path: '/aquamate/predict',
    handler: async (request, h) => {
      try {
        const predict = await predictDB.getAll();
        return h.response({
          status: true,
          data: predict
        }).code(200);
      } catch (e) {
        return h.response({
          status: false,
          message: "Unable to get the prediction details"
        }).code(500);
      }
    }
  //   ,
  //   options: {
  //     auth: 'jwt'
  // }
}
 
  ];
  
  module.exports = predictRoutes;