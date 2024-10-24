import React, { useState, useEffect } from 'react';
import axios from 'axios';
import StudentForm from './components/StudentForm';
import StudentList from './components/StudentList';
import './App.css';

const App = () => {
  const [students, setStudents] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  const fetchStudents = async () => {
    const response = await axios.get('localhost:3000/students');
    setStudents(response.data);
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  const handleSave = () => {
    setIsEditing(false);
    setSelectedStudent(null);
    fetchStudents();
  };

  const handleEdit = (student) => {
    setSelectedStudent(student);
    setIsEditing(true);
  };

  const handleDelete = async (id) => {
    await axios.delete(`localhost:3000/students/${id}`);
    fetchStudents();
  };

  return (
    <div className="app">
      <h1>Quản lý sinh viên</h1>
      {isEditing ? (
        <StudentForm 
          selectedStudent={selectedStudent} 
          onSave={handleSave} 
          onCancel={() => setIsEditing(false)} 
        />
      ) : (
        <button onClick={() => setIsEditing(true)}>Thêm sinh viên</button>
      )}
      <StudentList 
        students={students} 
        onEdit={handleEdit} 
        onDelete={handleDelete} 
      />
    </div>
  );
};

export default App;