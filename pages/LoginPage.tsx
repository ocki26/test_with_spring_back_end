import React, { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { MusicIcon } from "../components/Icons";

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);
    try {
      await login(email, password);
      // Navigate is handled inside login
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#16a34a]/5 flex flex-col justify-center sm:py-12">
      <div className="p-10 xs:p-0 mx-auto md:w-full md:max-w-md">
        <div className="flex items-center justify-center gap-2 mb-5">
          <MusicIcon className="w-10 h-10 text-[#16a34a]" />
          <h1 className="font-bold text-center text-3xl text-[#1a3c34]">
            ShowAdmin
          </h1>
        </div>

        <div className="bg-white shadow w-full rounded-lg divide-y divide-[#16a34a]/20">
          <form onSubmit={handleSubmit} className="px-5 py-7">
            <label className="font-semibold text-sm text-[#1a3c34] pb-1 block">
              E-mail
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="border border-[#16a34a]/30 rounded-lg px-3 py-2 mt-1 mb-5 text-sm w-full focus:outline-none focus:ring-2 focus:ring-[#16a34a] focus:border-transparent"
              placeholder="admin@example.com"
              required
            />
            <label className="font-semibold text-sm text-[#1a3c34] pb-1 block">
              Mật khẩu
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="border border-[#16a34a]/30 rounded-lg px-3 py-2 mt-1 mb-5 text-sm w-full focus:outline-none focus:ring-2 focus:ring-[#16a34a] focus:border-transparent"
              placeholder="********"
              required
            />

            {error && <p className="text-red-500 text-xs mb-4">{error}</p>}

            <button
              type="submit"
              disabled={isLoading}
              className="transition duration-200 bg-[#16a34a] hover:bg-[#15803d] focus:bg-[#15803d] focus:shadow-sm focus:ring-4 focus:ring-[#16a34a]/50 text-white w-full py-2.5 rounded-lg text-sm shadow-sm hover:shadow-md font-semibold text-center inline-block disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                "Đang đăng nhập..."
              ) : (
                <span className="inline-block mr-2">Đăng nhập</span>
              )}
            </button>
          </form>
          <div className="p-4">
            <p className="text-xs text-center text-[#16a34a]">Tài khoản mẫu:</p>
            <ul className="text-xs text-center text-[#16a34a] mt-1 space-y-1">
              <li>
                Admin:{" "}
                <span className="font-mono text-[#1a3c34]">
                  admin@example.com
                </span>{" "}
                / mk: <span className="font-mono text-[#1a3c34]">admin</span>
              </li>
              <li>
                Công ty:{" "}
                <span className="font-mono text-[#1a3c34]">
                  company_a@example.com
                </span>{" "}
                / mk:{" "}
                <span className="font-mono text-[#1a3c34]">company_a</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
