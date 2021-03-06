const express = require('express');
const app = express();
const Joi = require('joi');
const _ = require('lodash');
const mongoose = require('mongoose');
const {User, validate} = require('./models/user');
const bodyParser=require('body-parser');
const config=require('config');

if(!config.get('db'))
{
  console.error('FATAL ERROR: Database is not defined.');
  process.exit(1);
}

require('./prod')(app);

mongoose.connect(config.get('db'))
  .then(() => console.log('Connected to MongoDB...'))
  .catch(err => console.error('Could not connect to MongoDB...'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

app.get('/api/get',async(req,res)=>{
    res.json('RUNNING');
});

app.post('/api/register', async (req,res) => {
    const { error } = validate(req.body); 
    if (error) return res.json({status:'0', message: error.details[0].message});

    let user = await User.findOne({ phone: req.body.phone });
    if (user) return res.json({status:'0',message:'User already registered.'});

    user = new User(_.pick(req.body, ['name','phone']));
    await user.save();

    res.json({status:'1',message:'Registeration Success'});
});

app.post('/api/login', async (req,res) =>{
    const {error} = val(req.body);
    if (error) return res.json({status:'0', message: error.details[0].message});

    let user = await User.findOne({ phone: req.body.phone });
    if (!user) return res.json({status:'0',message:'Invalid phone number'});

    

    res.json({status:'1', message:'Login Success', name:user.name, phone:user.phone });
});

function val(req) {
    const schema = {
      phone: Joi.number().required()
    };
  
    return Joi.validate(req, schema);
  }

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));