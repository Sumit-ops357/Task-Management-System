'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { getTasks, createTask } from '@/lib/api';
import { Task } from '@/types';

export default function DashboardPage() {
  const router = useRouter();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [username, setUsername] = useState('');
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    status: 'Pending',
  });
  const [creating, setCreating] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('username');
    if (!token) {
      router.push('/login');
      return;
    }
    setUsername(user || '');
    fetchTasks(token);
  }, [router]);

  const fetchTasks = async (token: string) => {
    try {
      const data = await getTasks(token);
      setTasks(data);
    } catch {
      console.error('Failed to fetch tasks');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    if (!token) return;
    setCreating(true);
    setError('');
    try {
      const task = await createTask(token, newTask);
      setTasks((prev) => [...prev, task]);
      setNewTask({ title: '', description: '', status: 'Pending' });
      setShowForm(false);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create task');
    } finally {
      setCreating(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    router.push('/login');
  };

  const statusStyle = (status: string)=>
    status === 'Completed'
      ? 'bg-green-500/20 text-green-300 border-green-500/30'
      : 'bg-yellow-500/20 text-yellow-300 border-yellow-500/30';

  const pendingCount = tasks.filter((t) => t.status === 'Pending').length;
  const completedCount = tasks.filter((t) => t.status === 'Completed').length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      {/* Navbar */}
      <nav className="bg-white/5 backdrop-blur-md border-b border-white/10 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 bg-purple-600 rounded-lg flex items-center justify-center">
            <span className="text-white font-bold">TM</span>
          </div>
          <h1 className="text-white font-bold text-xl">Task Manager</h1>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-gray-400 text-sm">
            Hello,{' '}
            <span className="text-purple-400 font-semibold">{username}</span>
          </span>
          <button
            onClick={handleLogout}
            className="bg-red-500/20 hover:bg-red-500/30 border border-red-500/30 text-red-400 px-4 py-2 rounded-lg text-sm transition"
          >
            Logout
          </button>
        </div>
      </nav>

      <div className="max-w-5xl mx-auto px-6 py-10">
        {/* Stats */}
        <div className="grid grid-cols-3 gap-4 mb-8">
          <div className="bg-white/10 rounded-xl p-4 border border-white/20 text-center">
            <p className="text-3xl font-bold text-white">{tasks.length}</p>
            <p className="text-gray-400 text-sm mt-1">Total Tasks</p>
          </div>
          <div className="bg-yellow-500/10 rounded-xl p-4 border border-yellow-500/20 text-center">
            <p className="text-3xl font-bold text-yellow-300">{pendingCount}</p>
            <p className="text-gray-400 text-sm mt-1">Pending</p>
          </div>
          <div className="bg-green-500/10 rounded-xl p-4 border border-green-500/20 text-center">
            <p className="text-3xl font-bold text-green-300">{completedCount}</p>
            <p className="text-gray-400 text-sm mt-1">Completed</p>
          </div>
        </div>

        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-white">My Tasks</h2>
          <button
            onClick={() => setShowForm(true)}
            className="bg-purple-600 hover:bg-purple-700 text-white font-semibold px-5 py-2.5 rounded-xl transition flex items-center gap-2"
          >
            <span className="text-lg font-light">+</span> New Task
          </button>
        </div>

        {/* Create Task Modal */}
        {showForm && (
          <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50">
            <div className="bg-slate-800 border border-white/10 rounded-2xl p-8 w-full max-w-md shadow-2xl">
              <h3 className="text-xl font-bold text-white mb-6">Create New Task</h3>
              <form onSubmit={handleCreateTask} className="space-y-4">
                <div>
                  <label className="text-gray-300 text-sm mb-1 block">Title *</label>
                  <input
                    type="text"
                    value={newTask.title}
                    onChange={(e) =>
                      setNewTask((p) => ({ ...p, title: e.target.value }))
                    }
                    required
                    className="w-full bg-white/10 border border-white/20 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-purple-500 transition"
                    placeholder="e.g. Fix login bug"
                  />
                </div>
                <div>
                  <label className="text-gray-300 text-sm mb-1 block">Description</label>
                  <textarea
                    value={newTask.description}
                    onChange={(e) =>
                      setNewTask((p) => ({ ...p, description: e.target.value }))
                    }
                    className="w-full bg-white/10 border border-white/20 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-purple-500 transition resize-none"
                    placeholder="Optional details..."
                    rows={3}
                  />
                </div>
                <div>
                  <label className="text-gray-300 text-sm mb-1 block">Status</label>
                  <select
                    value={newTask.status}
                    onChange={(e) =>
                      setNewTask((p) => ({ ...p, status: e.target.value }))
                    }
                    className="w-full bg-slate-700 border border-white/20 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-purple-500 transition"
                  >
                    <option value="Pending">Pending</option>
                    <option value="Completed">Completed</option>
                  </select>
                </div>
                {error && <p className="text-red-400 text-sm">{error}</p>}
                <div className="flex gap-3 pt-2">
                  <button
                    type="button"
                    onClick={() => setShowForm(false)}
                    className="flex-1 bg-white/10 hover:bg-white/20 text-white py-3 rounded-lg transition"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={creating}
                    className="flex-1 bg-purple-600 hover:bg-purple-700 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50"
                  >
                    {creating ? 'Creating...' : 'Create'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Tasks List */}
        {loading ? (
          <div className="flex items-center justify-center py-20">
            <div className="text-gray-400 animate-pulse">Loading tasks...</div>
          </div>
        ) : tasks.length === 0 ? (
          <div className="text-center py-20">
            <div className="text-6xl mb-4">📋</div>
            <h3 className="text-white text-xl font-semibold">No tasks yet</h3>
            <p className="text-gray-400 mt-2">Click &quot;New Task&quot; to get started!</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            {tasks.map((task) => (
              <div
                key={task.id}
                className="bg-white/10 backdrop-blur-md border border-white/20 rounded-xl p-5 hover:border-purple-500/50 transition group"
              >
                <div className="flex items-start justify-between mb-3 gap-2">
                  <h3 className="text-white font-semibold text-base group-hover:text-purple-400 transition leading-snug">
                    {task.title}
                  </h3>
                  <span
                    className={`text-xs font-medium px-2.5 py-1 rounded-full border whitespace-nowrap ${statusStyle(task.status)}`}
                  >
                    {task.status}
                  </span>
                </div>
                {task.description && (
                  <p className="text-gray-400 text-sm leading-relaxed">
                    {task.description}
                  </p>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
