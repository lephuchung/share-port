const mongoose = require('mongoose');

const studentSchema = new mongoose.Schema({
    name: { type: String, required: true },
    dob: { type: Date, required: true },
    mssv: { type: String, required: true, unique: true }
});

const Student = mongoose.model('Student', studentSchema);

module.exports = Student;

