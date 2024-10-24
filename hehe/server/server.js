const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const cors = require('cors');
const students = require('./routes/api');
const Student = require('./models/student');
const MONGO_URI="mongodb+srv://lephuchung003:ZSqvyIlf1Mb7iHTa@students.65xge.mongodb.net/?retryWrites=true&w=majority&appName=students"

const PORT=3000

const app = express();

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Kết nối MongoDB
mongoose.connect(MONGO_URI)
  .then(() => console.log('Connected to MongoDB'))
  .catch((err) => console.error(err));

app.get('/', (req, res) => {
    res.send('Welcome to the Student CRUD App!');
  });

app.get('/students', async (req, res) => {
    try {
        const students = await Student.find();
        console.log(students);
        
        res.status(200).json(students);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
  });


// API Routes
app.use('/routes/api', students);

// Khởi động server
const port = PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));
