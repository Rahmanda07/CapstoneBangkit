const questionRoutes = require('./question');
const answerRoutes = require('./answer');
const predictRoutes = require('./predict')
const dictionaryRoutes = require('./dictionary');
const authRoutes = require('./auth');

const routes = [
    {
        method: 'GET',
        path: '/aquamate',
        handler: (request, h) => {
            return "aquamate backend";
        }
    },
    ...predictRoutes,
    ...questionRoutes,
    ...answerRoutes,
    ...dictionaryRoutes,
    // ...authRoutes
];

module.exports = routes;