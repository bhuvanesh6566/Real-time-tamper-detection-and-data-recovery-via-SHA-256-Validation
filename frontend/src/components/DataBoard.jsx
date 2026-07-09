import React from 'react';
import { ShieldAlert, ShieldCheck, RefreshCw, Trash2 } from 'lucide-react';

export default function DataBoard({ records, onDelete, onRecover }) {
  if (!records || records.length === 0) {
    return <div className="empty-state">No records found. Insert data above to start tracking.</div>;
  }

  return (
    <div className="data-grid">
      {records.map(record => (
        <div key={record.id} className="glass-panel data-card fade-in">
          <div className="card-header">
            <span className="card-id">Record #{record.id}</span>
            {record.status === 'VERIFIED' && (
              <span className="badge badge-verified"><ShieldCheck size={14}/> Verified</span>
            )}
            {record.status === 'TAMPERED' && (
              <span className="badge badge-tampered"><ShieldAlert size={14}/> Tampered</span>
            )}
            {record.status === 'RECOVERED' && (
              <span className="badge badge-recovered"><RefreshCw size={14}/> Recovered</span>
            )}
          </div>
          
          <div className="card-body">
            <div style={{display: 'flex', justifyContent: 'space-between', gap: '20px', marginBottom: '10px'}}>
              <div>
                <div className="value-lbl">Original Secure Data</div>
                <div className="value-val" style={{fontSize: '1.2rem'}}>
                  {record.originalStudentName} <br/>
                  <span style={{fontSize: '0.9rem', color: '#94a3b8'}}>Roll: {record.originalRollNumber} | Grade: {record.originalGrade}</span>
                </div>
              </div>
              
              <div>
                <div className="value-lbl">Current Database Data</div>
                <div className={`value-val ${record.status === 'TAMPERED' ? 'tampered-val' : ''}`} style={{fontSize: '1.2rem'}}>
                  {record.studentName} <br/>
                  <span style={{fontSize: '0.9rem', color: record.status === 'TAMPERED' ? '#ef4444' : '#94a3b8'}}>Roll: {record.rollNumber} | Grade: {record.grade}</span>
                  {record.status === 'TAMPERED' && <span style={{fontSize:'0.9rem', color: '#ef4444', marginLeft:'10px'}}><br/>(Altered!)</span>}
                </div>
              </div>
            </div>

            <div className="hash-lbl">Hash: {record.hash}</div>
          </div>

          <div className="card-actions">
            {record.status === 'TAMPERED' && (
              <button className="btn btn-recover" onClick={() => onRecover(record.id)} title="Auto-Heal this record">
                <RefreshCw size={14}/> Auto-Heal
              </button>
            )}
            <button className="btn btn-delete" onClick={() => onDelete(record.id)} title="Delete Record">
              <Trash2 size={16} />
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}
