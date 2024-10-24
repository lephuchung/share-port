import React from 'react';
import './StudentList.css';

const StudentList = ({ students, onEdit, onDelete }) => {
  return (
    <div className="student-list">
      <table>
        <thead>
          <tr>
            <th>Tên sinh viên</th>
            <th>Ngày sinh</th>
            <th>Mã số sinh viên</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student._id}>
              <td>{student.name}</td>
              <td>{new Date(student.dob).toLocaleDateString()}</td>
              <td>{student.mssv}</td>
              <td>
                <button onClick={() => onEdit(student)}>Sửa</button>
                <button onClick={() => onDelete(student._id)}>Xóa</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StudentList;