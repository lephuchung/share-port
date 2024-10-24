import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './StudentForm.css';

const StudentForm = ({ selectedStudent, onSave, onCancel }) => {
  const [name, setName] = useState('');
  const [dob, setDob] = useState('');
  const [mssv, setMssv] = useState('');

  useEffect(() => {
    if (selectedStudent) {
      setName(selectedStudent.name);
      setDob(selectedStudent.dob.split('T')[0]); // Format for input date field
      setMssv(selectedStudent.mssv);
    }
  }, [selectedStudent]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const studentData = { name, dob, mssv };
    
    if (selectedStudent) {
      await axios.put(`/api/students/${selectedStudent._id}`, studentData);
    } else {
      await axios.post('/api/students', studentData);
    }

    onSave();
  };

  return (
    <div className="student-form">
      <form onSubmit={handleSubmit}>
        <label>Tên sinh viên:</label>
        <input 
          type="text" 
          value={name} 
          onChange={(e) => setName(e.target.value)} 
          required
        />
        <label>Ngày sinh:</label>
        <input 
          type="date" 
          value={dob} 
          onChange={(e) => setDob(e.target.value)} 
          required
        />
        <label>Mã số sinh viên:</label>
        <input 
          type="text" 
          value={mssv} 
          onChange={(e) => setMssv(e.target.value)} 
          required
        />
        <button type="submit">Lưu</button>
        <button type="button" onClick={onCancel}>Hủy</button>
      </form>
    </div>
  );
};

export default StudentForm;
