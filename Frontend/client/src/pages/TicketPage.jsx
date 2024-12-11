import { Link } from "react-router-dom";
import {IoMdArrowBack} from 'react-icons/io'
import {RiDeleteBinLine} from 'react-icons/ri'
import { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

export default function TicketPage() {
  
    const [userTickets, setUserTickets] = useState([]);
  
    useEffect(() => {
      const fetchTickets = async () => {
        try {
          const user = localStorage.getItem("user");
          let token = null;
  
          if (user) {
            const parsedUser = JSON.parse(user); // Parse the JSON
            token = parsedUser.access_token; // Extract the token
            console.log(" token" ,token);
  
            // Make the API call
            const response = await axios.get("http://localhost:8080/api/tickets/getTicket", {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
            console.log("Request headers:", {
              Authorization: `Bearer ${token}`,
            });
            console.log("Response data:", response.data);
            setUserTickets(response.data); // Update the state with the fetched data
          } else {
            console.error("No user found in local storage.");
          }
        } catch (error) {
          console.error("Error fetching events:", error);
        }
      };
  
      fetchTickets(); // Call the async function
    }, []);
  
    const deleteTicket = async(ticketId) => {
      const user = localStorage.getItem("user");
      let token = null;

      if (user) {
        const parsedUser = JSON.parse(user);
        token = parsedUser.access_token;
      }

      if (!token) {
        console.error("No access token found.");
        return;
      }
      try {
        const response = await fetch(`http://localhost:8080/api/tickets/deleteTicket/${ticketId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });        
        if(response.ok){
          toast.success('Ticket deleted successfully');
          setUserTickets(userTickets.filter(ticket => ticket.id !== ticketId));
        }
      } catch (error) {
        toast.error('Ticket deleted unsuccessfully');
      }
    }
  
    return (
      <div className="flex flex-col flex-grow">
      <div className="mb-5 flex justify-between place-items-center">
        <div>
          <Link to='/'>
            <button 
                // onClick={handleBackClick}
                className='
                inline-flex 
                mt-12
                gap-2
                p-3 
                ml-12
                bg-gray-100
                justify-center 
                items-center 
                text-blue-700
                font-bold
                rounded-md'
                >
              <IoMdArrowBack 
              className='
              font-bold
              w-6
              h-6
              gap-2'/> 
              Back
            </button>
          </Link>
        </div>
        <div className=" place-item-center hidden">
          
            <RiDeleteBinLine className="h-6 w-10 text-red-700 "/>
          
        </div>
        
        </div>
        <div className="mx-12 grid grid-cols-1 xl:grid-cols-2 gap-5">
          
        {userTickets.map(ticket => (
          
        <div key={ticket.id} >
          <div className="">
            
            <div className="h-48 mt-2 gap-2 p-5 bg-gray-100 font-bold rounded-md relative">
              <button onClick={()=>deleteTicket(ticket.id)} className="absolute cursor-pointer right-0 mr-2">
                <RiDeleteBinLine className=" h-6 w-10 text-red-700 "/>
              </button>
              <div className="flex justify-start place-items-center text-sm md:text-base font-normal">
                
                <div className=" h-148 w-148">
                  {/* <img src={ticket.ticketDetails.qr} alt="QRCode" className="aspect-square object-fill "/> */}
                </div>
                <div className="ml-6 grid grid-cols-2 gap-x-6 gap-y-2">
                  <div className="">
                    Event Name : <br /><span className=" font-extrabold text-primarydark">{ticket.eventName.toUpperCase()}</span>
                  </div>
                  
                  <div>
                    Date & Time:<br /> <span className="font-extrabold text-primarydark">{ticket.eventDate.toUpperCase().split("T")[0]}, {ticket.eventTime}</span>
                  </div>
                  <div>
                    Name: <span className="font-extrabold text-primarydark">{ticket.name.toUpperCase()}</span>
                  </div>
                  <div>
                    Total Price: <span className="font-extrabold text-primarydark"> Rs. {ticket.totalPrice}</span>
                  </div>
                  <div>
                    Email: <span className="font-extrabold text-primarydark">{ticket.email}</span>
                  </div>
                  <div>
                    Ticket ID:<br /><span className="font-extrabold text-primarydark">{ticket.id}</span>
                  </div>
                </div>
              </div>
              
            </div>
          </div>
          </div>
        
         ))}
         </div>
  
      
      </div>
    )
}
