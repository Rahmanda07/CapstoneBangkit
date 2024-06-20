const questionDB = require('../models/question');
const uploadImage = require('../services/uploadImage');
const Joi = require('joi');

const questionRoutes = [
  {
    method: 'POST',
    path: '/aquamate/questions',
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
          questionName: Joi.string().required(),
          questionImage: Joi.any().required(),
        }),
      },
    },
    handler: async (request, h) => {
      try {
        const { questionName, questionImage } = request.payload;
        const imageUrl = await uploadImage(questionImage);

        const id = await questionDB.create({
          questionName,
          questionUrl: imageUrl,
        });

        return h.response({
          status: true,
          message: "Question added successfully",
          id: id,
        }).code(201);
      } catch (e) {
        console.error(e);
        return h.response({
          status: false,
          message: "Error while adding question",
        }).code(500);
      }
    },
  },
    {
      method: 'GET',
      path: '/aquamate/questions',
      handler: async (request, h) => {
        try {
          const questions = await questionDB.getAllWithAnswers();
          return h.response(questions).code(200);
        } catch (e) {
          return h.response({
            status: false,
            message: "Unable to get the question details"
          }).code(500);
        }
      }
    //   ,
    //   options: {
    //     auth: 'jwt'
    // }
    },
    {
      method: 'GET',
      path: '/aquamate/questions/{id}',
      handler: async (request, h) => {
        try {
          const id = request.params.id;
          const questionEntry = await questionDB.getById(id);
          return h.response({
            status: true,
            data: questionEntry
          }).code(200);
        } catch (e) {
          return h.response({
            status: false,
            message: "Unable to get the question entry details"
          }).code(500);
        }
      }
    //   ,
    //   options: {
    //     auth: 'jwt'
    // }
    },
    {
      method: 'DELETE',
      path: '/aquamate/questions/{id}',
      handler: async (request, h) => {
        try {
          const id = request.params.id;
          await questionDB.delete(id);
          return h.response({
            status: true,
            message: "Question and its related answers deleted successfully"
          }).code(200);
        } catch (e) {
          return h.response({
            status: false,
            message: "Error while deleting question"
          }).code(500);
        }
      }
    //   ,
    //   options: {
    //     auth: 'jwt'
    // }
    }
  ];
  
  module.exports = questionRoutes;