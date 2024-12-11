/* eslint-disable no-empty */
import { Link, Navigate } from "react-router-dom";
import { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

export default function RegisterPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [mobile, setMobile] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [address, setAddress] = useState("");
  const [redirect, setRedirect] = useState(false);

  async function registerUser(ev) {
    ev.preventDefault();

    // Validate password and confirm password
    if (password !== confirmPassword) {
      toast.error("Passwords do not match");
      return;
    }

    try {
      // Make API call
      await axios.post(`http://localhost:8080/sign-up`, {
        userName: name,
        userEmail: email,
        userMobileNo: mobile,
        userPassword: password,
        userRole: "ROLE_VENDER",
        address: address,
      });
      toast.success("Registration successfully!");
      setRedirect(true);
    } catch (e) {
      toast.error("Registration unsuccessfully!");
      // alert("Registration failed: " + (e.response?.data?.message || e.message));
    }
  }

  if (redirect) {
    return <Navigate to={"/login"} />;
  }

  return (
    <div className="flex w-full h-full lg:-ml-24 px-10 py-10 justify-between place-items-center mt-12">
      <div className="hidden lg:flex flex-col right-box ">
        <div className="flex flex-col gap-3">
          <div className="text-3xl font-black">Welcome to</div>
          <div>
            <img src="../src/assets/logo.png" alt="" className="w-48" />
          </div>
        </div>

        <div className="ml-48 w-80 mt-6">
          <img src="../src/assets/signuppic.svg" alt="" className="w-full" />
        </div>
      </div>

      <div className="bg-white w-full sm:w-full md:w-1/2 lg:w-1/3 px-7 py-7 rounded-xl justify-center align-middle ">
        <form className="flex flex-col w-auto items-center" onSubmit={registerUser}>
          <h1 className="px-3 font-extrabold mb-5 text-primarydark text-2xl">Sign Up</h1>

          {/* Name */}
          <div className="input">
            <input
              type="text"
              placeholder="Name"
              className="input-et"
              value={name}
              onChange={(ev) => setName(ev.target.value)}
              required
            />
          </div>

          {/* Email */}
          <div className="input">
            <input
              type="email"
              placeholder="Email"
              className="input-et"
              value={email}
              onChange={(ev) => setEmail(ev.target.value)}
              required
            />
          </div>

          {/* Mobile Number */}
          <div className="input">
            <input
              type="text"
              placeholder="Mobile Number"
              className="input-et"
              value={mobile}
              onChange={(ev) => setMobile(ev.target.value)}
              required
            />
          </div>

          {/* Password */}
          <div className="input">
            <input
              type="password"
              placeholder="Password"
              className="input-et"
              value={password}
              onChange={(ev) => setPassword(ev.target.value)}
              required
            />
          </div>

          {/* Confirm Password */}
          <div className="input">
            <input
              type="password"
              placeholder="Confirm Password"
              className="input-et"
              value={confirmPassword}
              onChange={(ev) => setConfirmPassword(ev.target.value)}
              required
            />
          </div>

          {/* Address */}
          <div className="input">
            <input
              type="text"
              placeholder="Address"
              className="input-et"
              value={address}
              onChange={(ev) => setAddress(ev.target.value)}
              required
            />
          </div>

          <div className="w-full py-4">
            <button type="submit" className="primary w-full">
              Create Account
            </button>
          </div>

          <div className="container2 ">
              <div className="w-full h-full p-1">
                <Link to={'/login'}>
                  <button type="submit" className="text-white cursor-pointer rounded w-full h-full font-bold" > Sign In</button>
                </Link>
              </div>
              <div className="w-full h-full p-1">
                <Link to={'/register'}>
                  <button type="submit" className="text-black cursor-pointer rounded w-full h-full bg-primary font-bold" > Sign Up</button>
                </Link>
              </div>
            </div>

            <Link to={'/'} className="">
              <button className="secondary">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4">
                  <path fillRule="evenodd" d="M11.03 3.97a.75.75 0 010 1.06l-6.22 6.22H21a.75.75 0 010 1.5H4.81l6.22 6.22a.75.75 0 11-1.06 1.06l-7.5-7.5a.75.75 0 010-1.06l7.5-7.5a.75.75 0 011.06 0z" clipRule="evenodd" />
                </svg> 
                Back 
              </button>
            </Link>
        </form>
      </div>
    </div>
  );
}
