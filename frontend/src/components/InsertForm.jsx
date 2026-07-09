import React, { useState } from 'react';
import { Plus } from 'lucide-react';

export default function InsertForm({ onInsert }) {
  const [studentName, setStudentName] = useState('');
  const [rollNumber, setRollNumber] = useState('');
  const [grade, setGrade] = useState('');

  const submit = (e) => {
    e.preventDefault();
    if (!studentName || !rollNumber || isNaN(rollNumber) || !grade) return;
    onInsert({ 
        studentName, 
        rollNumber: Number(rollNumber), 
        grade 
    });
    setStudentName('');
    setRollNumber('');
    setGrade('');
  };

  return (
    <div>
      <h3>Add Secured Student Data</h3>
      <form onSubmit={submit} className="insert-form" style={{display: 'flex', gap: '10px', flexWrap: 'wrap', alignItems: 'center'}}>
        <input 
          type="text" 
          className="insert-input" 
          placeholder="Student Name" 
          value={studentName}
          onChange={e => setStudentName(e.target.value)}
        />
        <input 
          type="number" 
          className="insert-input" 
          placeholder="Roll Number" 
          value={rollNumber}
          onChange={e => setRollNumber(e.target.value)}
        />
        <input 
          type="text" 
          className="insert-input" 
          placeholder="Grade (e.g., A+)" 
          value={grade}
          onChange={e => setGrade(e.target.value)}
        />
        <button type="submit" className="btn" disabled={!studentName || !rollNumber || !grade}>
          <Plus size={18} /> Secure Student
        </button>
      </form>
    </div>
  );
}
