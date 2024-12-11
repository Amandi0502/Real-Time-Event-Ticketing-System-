/* eslint-disable react/jsx-key */
import axios from "axios";
import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import { BsArrowRightShort } from "react-icons/bs";
import { BiLike } from "react-icons/bi";

  export default function IndexPage() {
    const [events, setEvents] = useState([]);

    useEffect(() => {
      const fetchEvents = async () => {
        try {
          const user = localStorage.getItem("user");
          let token = null;
  
          if (user) {
            const parsedUser = JSON.parse(user); // Parse the JSON
            token = parsedUser.access_token; // Extract the token
            console.log(" token" ,token);
  
            // Make the API call
            const response = await axios.get("http://localhost:8080/api/events/all", {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
            console.log("Request headers:", {
              Authorization: `Bearer ${token}`,
            });
            console.log("Response data:", response.data);
            setEvents(response.data); // Update the state with the fetched data
          } else {
            console.error("No user found in local storage.");
          }
        } catch (error) {
          console.error("Error fetching events:", error);
        }
      };
  
      fetchEvents(); // Call the async function
    }, []); // Empty dependency array ensures this runs once on mount
    
  //! Like Functionality --------------------------------------------------------------
    const handleLike = (eventId) => {
      axios
        .post(`/event/${eventId}`)
        .then((response) => {
            setEvents((prevEvents) =>
            prevEvents.map((event) =>
              event._id === eventId
                ? { ...event, likes: event.likes + 1 }
                : event
            )
          );
          console.log("done", response)
        })
        .catch((error) => {
          console.error("Error liking ", error);
        });
    };
  

    return (
      <div className="mt-1 flex flex-col">
        <div className="hidden sm:block" >
          <div href="#" className="flex item-center inset-0">
            <img src="../src/assets/hero3.png" alt="" className='w-full'/> 
          </div>
        </div>
        <h1 className="font-bold text-lg mt-16 mb-2 ml-16">UPCOMING EVENTS</h1>
        <div className="mx-10 my-5 grid gap-x-6 gap-y-8 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:mx-5 ">
        
        {/*-------------------------- Checking whether there is a event or not-------------------  */}
        {events.length > 0 && events.map((event) => {
          const eventDate = new Date(event.eventDate);
          const currentDate = new Date();
          
          //! Check the event date is passed or not --------------------------------------------------------------------------------------- 
          if (eventDate > currentDate || eventDate.toDateString() === currentDate.toDateString()){
            return (
              <div className="bg-white rounded-xl relative" key={event._id}>
              <div className='rounded-tl-[0.75rem] rounded-tr-[0.75rem] rounded-br-[0] rounded-bl-[0] object-fill aspect-16:9'>
              {event.image && (
                <img
                  src={`${event.image}`}
                  alt={event.title}
                  width="300" 
                  height="200" 
                  className="w-full h-full"
                />
              )}
              </div>                
                {/* <img src="../src/assets/paduru.png" alt="" className='rounded-tl-[0.75rem] rounded-tr-[0.75rem] rounded-br-[0] rounded-bl-[0] object-fill aspect-16:9'/>  */}
    {/* FIXME: This is a demo image after completing the create event function delete this */}

              <div className="m-2 grid gap-2">
                <div className="flex justify-between items-center">
                  {/* <h1 className="font-bold text-lg mt-2">{event.title.toUpperCase()}</h1> */}
                  <div className="flex gap-2 items-center mr-4 text-red-600"> <BiLike /> {event.likes}</div>
                </div>
                

                <div className="flex text-sm flex-nowrap justify-between text-primarydark font-bold mr-4">
                  <div>{event.eventDate.split("T")[0]}, {event.eventTime}</div>
                  <div>{event.ticketPrice === 0? 'Free' : 'Rs. '+ event.ticketPrice}</div>
                </div>

                <div className="text-xs flex flex-col flex-wrap truncate-text">{event.description}</div>
                <div className="flex justify-between items-center my-2 mr-4">
                  <div className="text-sm text-primarydark ">Organized By: <br /><span className="font-bold">{event.organizedBy}</span></div>
                  {/* <div className="text-sm text-primarydark ">Created By: <br/> <span className="font-semibold">{event.owner.toUpperCase()}</span></div> */}
                </div>
                <Link to={'/event/'+event.id} className="flex justify-center">
                  <button className="primary flex items-center gap-2">Book Ticket< BsArrowRightShort className="w-6 h-6" /></button>
                </Link>
                
              </div>
            </div>
            )
          }
          return null;
        }   
        )}
        </div>
      </div>        
      )
  }
  