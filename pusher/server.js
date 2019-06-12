require('dotenv').config();
const express = require('express');
const cors = require('cors');

const bodyParser = require('body-parser');
const Pusher = require('pusher');

const app = express();
const port = process.env.PORT || 4000;
const pusher = new Pusher({
    appId: process.env.PUSHER_APP_ID,
    key: process.env.PUSHER_KEY,
    secret: process.env.PUSHER_SECRET,
    cluster: 'ap3',
});

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header(
        'Access-Control-Allow-Headers',
        'Origin, X-Requested-With, Content-Type, Accept'
    );
    next();
});

app.listen(port, () => {
    console.log(`Server started on port ${port}`);
});

app.post('/pusher/auth', function(req, res) {
    var socketId = req.body.socket_id;
    var channel = req.body.channel_name;
    var presenceData = {
        user_id: 'unique_user_id',
        user_info: {
            name: 'Mr Channels',
            twitter_id: '@pusher'
        }
    };
    var auth = pusher.authenticate(socketId, channel, presenceData);
    res.send(auth);
});

app.post('/paint', (req, res) => {
    pusher.trigger('presence-event', 'draw', req.body);
    res.json(req.body);
});

// app.get('/user', (req, res) => {
//     // pusher.get({path: '/channels', params: {}}, function (error, request, response) {
//     //     console.log(response.statusCode)
//     //     if (response.statusCode === 200) {
//     //         var result = JSON.parse(response.body);
//     //         var channelsInfo = result.channels;
//     //         console.log(channelsInfo)
//     //         pusher.trigger('users', 'user', response);
//     //     }
//     // });
//
//     // pusher.get({path: '/channels/painting', params: {}}, function (error, request, response) {
//     //     // console.log("channel name: " + response.statusCode);
//     //     if (response.statusCode === 200) {
//     //         var result = JSON.parse(response.body);
//     //         var channelsInfo = result.channels;
//     //         console.log(response.body)
//     //         pusher.trigger('users', 'user', response);
//     //
//     //         // pusher.trigger('painting', 'draw', result);
//     //     }
//     // });
//
//     // pusher.get({path: '/channels/painting/users', params: {}},
//     //     function (error, request, response) {
//     //         console.log("users: " + response.statusCode);
//     //         if (response.statusCode === 200) {
//     //             var result = JSON.parse(response.body);
//     //             var users = result.users;
//     //             console.log(response.body)
//     //             pusher.trigger('users', 'user', response);
//     //
//     //         }
//     //     });
//
//     res.json(req.body);
// });
