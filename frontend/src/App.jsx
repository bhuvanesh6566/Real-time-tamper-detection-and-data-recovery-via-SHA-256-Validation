import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { Toaster, toast } from 'react-hot-toast';
import { Database, ShieldCheck, ShieldOff } from 'lucide-react';
import InsertForm from './components/InsertForm';
import DataBoard from './components/DataBoard';

const API_BASE = 'http://localhost:8080/api/records';

function App() {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [secureMode, setSecureMode] = useState(false);
  const secureModeRef = useRef(false);
  const prevRecordsRef = useRef([]);

  const fetchRecords = async () => {
    try {
      const res = await axios.get(`${API_BASE}/data?secureMode=${secureModeRef.current}`);
      const newRecords = res.data;
      
      if (prevRecordsRef.current.length > 0) {
          newRecords.forEach(nr => {
              const old = prevRecordsRef.current.find(r => r.id === nr.id);
              if (old && old.status !== 'TAMPERED' && nr.status === 'TAMPERED') {
                  toast.error(`Alert: Record ${nr.id} was tampered with! Hash verification failed.`, { 
                      duration: 6000, 
                      style: { background: '#ef4444', color: '#fff', fontWeight: 'bold' } 
                  });
              }
          });
      }
      prevRecordsRef.current = newRecords;
      setRecords(newRecords);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecords();
    const interval = setInterval(fetchRecords, 2000);
    return () => clearInterval(interval);
  }, []);

  const handleInsert = async (payload) => {
    try {
      await axios.post(`${API_BASE}/insert`, payload);
      toast.success("Record inserted and secured with SHA-256.", {
          style: { background: '#10b981', color: '#fff' }
      });
      fetchRecords();
    } catch (err) {
      toast.error("Failed to insert secure record");
    }
  };

  const handleDelete = async (id) => {
      try {
          await axios.delete(`${API_BASE}/${id}`);
          fetchRecords();
      } catch (err) {
          console.error(err);
      }
  };

  const handleRecover = async (id) => {
    try {
      await axios.post(`${API_BASE}/recover/${id}`);
      toast.success(`Record ${id} auto-healed and restored.`, {
        style: { background: '#10b981', color: '#fff' }
      });
      fetchRecords();
    } catch (err) {
      toast.error('Auto-heal failed');
    }
  };

  return (
    <div className="app-container">
      <Toaster position="top-right" />
      <header>
        <h1><ShieldCheck size={40} style={{verticalAlign: 'bottom', marginRight: '10px'}}/> Self-Healing Database</h1>
        <p className="subtitle">Real-time tamper detection & secure data recovery via SHA-256 Validation.</p>
      </header>

      <div className="insert-form-container glass-panel">
        <InsertForm onInsert={handleInsert} />
      </div>

      <div className="glass-panel">
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem'}}>
          <h2 style={{display: 'flex', alignItems: 'center', gap: '0.5rem', margin: 0}}>
            <Database /> Immutable Ledger
          </h2>
          <button
            className={`btn ${secureMode ? 'btn-recover' : 'btn-delete'}`}
            onClick={() => {
              const next = !secureMode;
              secureModeRef.current = next;
              setSecureMode(next);
              toast(next ? '🔒 Secure Mode ENABLED — only verified data will be stored.' : '🔓 Secure Mode DISABLED.', {
                style: { background: next ? '#10b981' : '#f59e0b', color: '#fff' }
              });
            }}
          >
            {secureMode ? <><ShieldCheck size={14}/> Secure Mode: ON</> : <><ShieldOff size={14}/> Secure Mode: OFF</>}
          </button>
        </div>
        {loading ? <p style={{textAlign:"center", color:"#94a3b8"}}>Loading verified records...</p> : 
          <DataBoard records={records} onDelete={handleDelete} onRecover={handleRecover} />
        }
      </div>
    </div>
  );
}

export default App;
