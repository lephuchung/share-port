const express = require('express');
const router = express.Router();
const Student = require('../models/student.js');

/// CREATE student
router.post('/students', async (req, res) => {
    try {
        const { name, dob, mssv } = req.body;
        const newStudent = new Student({ name, dob, mssv });
        await newStudent.save();
        res.status(201).json(newStudent);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// READ all students
router.get('/students', async (req, res) => {
    try {
        const students = await Student.find();
        res.status(200).json(students);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// READ one student by ID
router.get('/students/:id', async (req, res) => {
    try {
        const student = await Student.findById(req.params.id);
        if (!student) return res.status(404).json({ message: "Student not found" });
        res.status(200).json(student);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// UPDATE student by ID
router.put('/students/:id', async (req, res) => {
    try {
        const { name, dob, mssv } = req.body;
        const updatedStudent = await Student.findByIdAndUpdate(
            req.params.id, { name, dob, mssv }, { new: true }
        );
        if (!updatedStudent) return res.status(404).json({ message: "Student not found" });
        res.status(200).json(updatedStudent);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// DELETE student by ID
router.delete('/students/:id', async (req, res) => {
    try {
        const deletedStudent = await Student.findByIdAndDelete(req.params.id);
        if (!deletedStudent) return res.status(404).json({ message: "Student not found" });
        res.status(200).json({ message: "Student deleted successfully" });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
