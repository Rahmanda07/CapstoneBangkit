const Joi = require('joi');
const Jwt = require('@hapi/jwt');
const User = require('../models/user');
const bcrypt = require('bcrypt');

const authRoutes = [
  {
    method: 'POST',
    path: '/aquamate/auth/signup',
    options: {
      validate: {
        payload: Joi.object({
          username: Joi.string().min(3).required(),
          password: Joi.string().min(6).required()
        })
      },
      auth: false
    },
    handler: async (request, h) => {
      const { username, password } = request.payload;
      try {
        const user = await User.findByUsername(username);
        if (user) {
          return h.response({ message: 'Username already exists' }).code(409);
        }
        await User.create(username, password);
        return h.response({ message: 'User registered successfully' }).code(201);
      } catch (error) {
        return h.response({ message: error.message }).code(500);
      }
    }
  },
  {
    method: 'POST',
    path: '/aquamate/auth/login',
    options: {
      validate: {
        payload: Joi.object({
          username: Joi.string().min(3).required(),
          password: Joi.string().min(6).required()
        })
      },
      auth: false
    },
    handler: async (request, h) => {
      const { username, password } = request.payload;
      try {
        const user = await User.findByUsername(username);
        if (!user) {
          return h.response({ message: 'Invalid username or password' }).code(401);
        }

        const isValid = await bcrypt.compare(password, user.password);
        if (!isValid) {
          return h.response({ message: 'Invalid username or password' }).code(401);
        }

        const token = Jwt.token.generate(
          {
            aud: 'urn:audience:test',
            iss: 'urn:issuer:test',
            user: username,
            group: 'hapi_community'
          },
          {
            key: 'UswB8Kzwwm',
            algorithm: 'HS256'
          }
        );

        return h.response({ token }).code(200);
      } catch (error) {
        return h.response({ message: error.message }).code(500);
      }
    }
  }
];

module.exports = authRoutes;