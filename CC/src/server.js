require('dotenv').config();
// const InputError = require("../exceptions/InputError");
const Hapi = require('@hapi/hapi');
const Jwt = require('@hapi/jwt');
const loadModel = require('./services/loadModel');
const routes = require('./routes/index');
 
(async () => {
    const server = Hapi.server({
        port: 5000,
        host: '0.0.0.0',
        routes: {
            cors: {
              origin: ['*'],
            },
        },
    });

        // // Register JWT plugin
        // await server.register(Jwt);
    
        // // Define JWT authentication strategy
        // server.auth.strategy('jwt', 'jwt', {
        //     keys: 'UswB8Kzwwm', // Mengambil secret key dari .env
        //     verify: {
        //         aud: false,
        //         iss: false,
        //         sub: false,
        //         nbf: true,
        //         exp: true,
        //         maxAgeSec: 14400, // 4 hours
        //         timeSkewSec: 15
        //     },
        //     validate: (artifacts, request, h) => {
        //         return {
        //             isValid: true,
        //             credentials: { user: artifacts.decoded.payload.user }
        //         };
        //     }
        // });
    
        // server.auth.default('jwt');

   
    const model = await loadModel();
    server.app.model = model;
 
   server.route(routes);
 
    // server.ext('onPreResponse', function (request, h) {
    //     const response = request.response;
 
    //     if (response instanceof InputError) {
    //         const newResponse = h.response({
    //             status: 'fail',
    //             message: `${response.message} Silakan gunakan foto lain.`
    //         })
    //         newResponse.code(response.statusCode)
    //         return newResponse;
    //     }
 
    //     if (response.isBoom) {
    //         const { statusCode } = response.output;
    //         const newResponse = h.response({
    //             status: 'fail',
    //             message: response.message
    //         }).code(statusCode);
    //         return newResponse;
    //     }
 
    //     return h.continue;
    // });
 
    await server.start();
    console.log(`Server start at: ${server.info.uri}`);
})();