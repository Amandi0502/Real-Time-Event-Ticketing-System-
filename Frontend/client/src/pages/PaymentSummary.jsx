import axios from 'axios';
import { useContext, useEffect, useState } from 'react';
import { Link, Navigate, useParams } from 'react-router-dom';
import { IoMdArrowBack } from 'react-icons/io';
import { UserContext } from '../UserContext';
import Qrcode from 'qrcode';
import { toast } from 'react-toastify';

export default function PaymentSummary() {
  const { id } = useParams();
  const [event, setEvent] = useState(null);
  const { user } = useContext(UserContext);
  const [details, setDetails] = useState({
    name: '',
    email: '',
    contactNo: '',
  });

  const defaultTicketState = {
    eventId: '',
    name:'',
    email: '',
    contactNo: '',
    eventName: '',
    eventDate: '',
    eventTime: '',
    quantity: 1,
    totalPrice : '',
  };

  const [ticketDetails, setTicketDetails] = useState(defaultTicketState);
  const [payment, setPayment] = useState({
    nameOnCard: '',
    cardNumber: '',
    expiryDate: '',
    cvv: '',
  });
  const [redirect, setRedirect] = useState(false);

  useEffect(() => {
    if (!id) {
      return;
    }

    // Get the token from localStorage
    const user = localStorage.getItem('user');
    let token = null;

    if (user) {
      const parsedUser = JSON.parse(user);
      token = parsedUser.access_token;
    }

    if (!token) {
      console.error('No access token found.');
      return;
    }

    axios
      .get(`http://localhost:8080/api/events/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`, // Include the token in the Authorization header
        },
      })
      .then((response) => {
        const eventData = response.data;

        setEvent(eventData);

        setTicketDetails((prevTicketDetails) => ({
          ...prevTicketDetails,
          eventId: eventData.id, 
          eventName: eventData.title,
          eventDate: eventData.eventDate.split('T')[0],
          eventTime: eventData.eventTime,
        }));        
      })
      .catch((error) => {
        console.error('Error fetching event data:', error);
        toast.error('Error fetching event details. Please try again.');
      });
  }, [id]);

  useEffect(() => {
    setTicketDetails((prevTicketDetails) => ({
      ...prevTicketDetails,
      name: details.name,   
      email: details.email, 
      contactNo: details.contactNo,
    }));
  }, [details]);

  if (!event) return '';

  const handleChangeDetails = (e) => {
    const { name, value } = e.target;
    setDetails((prevDetails) => ({
      ...prevDetails,
      [name]: value,
    }));
  };

  const handleChangePayment = (e) => {
    const { name, value } = e.target;
    setPayment((prevPayment) => ({
      ...prevPayment,
      [name]: value,
    }));
  };

  const createTicket = async (e) => {
    e.preventDefault();

    if (!id) {
      return;
    }

    // Get the token from localStorage
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

      const updatedTicketDetails = {
        ...ticketDetails, // Spread the top-level `ticketDetails` state
        quantity: ticketDetails.quantity, // Ensure quantity is included
        totalPrice: calculateTotalPrice(), // Calculate and include total price
      };

      delete updatedTicketDetails.ticketDetails;
      
      const response = await fetch('http://localhost:8080/api/tickets/purchase', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(updatedTicketDetails)
      });

      if (response.ok) {
        const contentType = response.headers.get('Content-Type');
        let responseData;

        if (contentType && contentType.includes('application/json')) {
          responseData = await response.json();
          console.log("Ticket purchase successfully:", responseData);
        } else {
          // If the response is not JSON, handle accordingly (e.g., log the text or HTML)
          const responseText = await response.text();
          console.log("Event posted, response:", responseText);
        }
        setRedirect(true);
        toast.success("Ticket purchase successfully!");
      } else {
        // Handle unsuccessful response
        console.error("Failed to post event:", response);
        toast.error("Failed to purchase ticket.");
      }
    } catch (error) {
      console.error('Error purchase ticket:', error);
    }
  };

  if (redirect) {
    return <Navigate to={'/wallet'} />;
  }

  // Calculate the total price based on quantity
  const calculateTotalPrice = () => {
    return ticketDetails.quantity * event.ticketPrice;
  };

  const handleQuantityChange = (e) => {
    const quantity = parseInt(e.target.value, 10);
    if (quantity >= 1) {
      setTicketDetails((prevTicketDetails) => ({
        ...prevTicketDetails,
        quantity,
      }));
    }
  };

  return (
    <>
      <div>
        <Link to={'/event/' + event.id + '/ordersummary'}>
          <button
            className="inline-flex mt-12 gap-2 p-3 ml-12 bg-gray-100 justify-center items-center text-blue-700 font-bold rounded-sm"
          >
            <IoMdArrowBack className="font-bold w-6 h-6 gap-2" />
            Back
          </button>
        </Link>
      </div>

      <div className="ml-12 bg-gray-100 shadow-lg mt-8 p-16 w-3/5 float-left">
        {/* Your Details */}
        <div className="mt-8 space-y-4">
            <h2 className="text-xl font-bold mb-4">Your Details</h2>
            <input
              type="text"
              name="name"
              value={details.name}
              onChange={handleChangeDetails}
              placeholder="Name"
              className="input-field ml-10 w-80 h-10 bg-gray-50 border border-gray-30  rounded-md p-2.5"
            />
            <input
              type="email"
              name="email"
              value={details.email}
              onChange={handleChangeDetails}
              placeholder="Email"
              className="input-field w-80 ml-3 h-10 bg-gray-50 border border-gray-30  rounded-sm p-2.5"
            />
            <div className="flex space-x-4">
            <input
              type="tel"
              name="contactNo"
              value={details.contactNo}
              onChange={handleChangeDetails}
              placeholder="Contact No"
              className="input-field ml-10 w-80 h-10 bg-gray-50 border border-gray-30 rounded-sm p-2.5"
            />
            </div>
          </div>

        {/* Payment Option */}
        <div className="mt-10 space-y-4">
          <h2 className="text-xl font-bold mb-4">Payment Option</h2>
          <div className="ml-10">
            <button
              type="button"
              className="px-8 py-3 text-black bg-blue-100 focus:outline border rounded-sm border-gray-300"
              disabled
            >
              Credit / Debit Card
            </button>
          </div>

          <input
            type="text"
            name="nameOnCard"
            value="A.B.S.L. Perera"
            onChange={handleChangePayment}
            placeholder="Name on Card"
            className="input-field w-80 ml-10 h-10 bg-gray-50 border border-gray-30  rounded-sm p-2.5"
          />
          <input
            type="text"
            name="cardNumber"
            value="5648 3212 7802"
            onChange={handleChangePayment}
            placeholder="Card Number"
            className="input-field w-80 ml-3 h-10 bg-gray-50 border border-gray-30 rounded-sm p-2.5"
          />
          <div className="flex space-x-4">
            <input
              type="text"
              name="expiryDate"
              value="12/25"
              onChange={handleChangePayment}
              placeholder="Expiry Date (MM/YY)"
              className="input-field w-60 ml-10 h-10 bg-gray-50 border border-gray-30  rounded-sm p-2.5"
            />
            <input
              type="text"
              name="cvv"
              value="532"
              onChange={handleChangePayment}
              placeholder="CVV"
              className="input-field w-60 ml-3 h-10 bg-gray-50 border border-gray-30 rounded-sm p-2.5"
            />
          </div>
        </div>

        <button
          onClick={createTicket}
          className="mt-10 bg-blue-700 text-white py-3 w-64"
        >
          Confirm Booking
        </button>
      </div>

      {/* Order Summary */}
      <div className="float-right bg-blue-100 w-1/4 p-5 mt-8 mr-12">
        <h2 className="text-xl font-bold">Order Summary</h2>
        <div className="mt-8">
          <h3>Event Name: {event.title}</h3>
          <h3>Date: {event.eventDate.split('T')[0]}</h3>
          <h3>Time: {event.eventTime}</h3>
          <h3>Ticket Price: ${event.ticketPrice}</h3>
        </div>
        <div className="mt-4">
          <label htmlFor="quantity">Quantity:</label>
          <input
            type="number"
            id="quantity"
            value={ticketDetails.quantity}
            onChange={handleQuantityChange}
            min="1"
            className="input-field w-24 ml-4 p-2"
          />
        </div>
        <div className="mt-4">
          <h3>Total Price: ${calculateTotalPrice()}</h3>
        </div>
      </div>
    </>
  );
}
