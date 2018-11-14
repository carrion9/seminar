
export function formatHumanDate(dateString) {
    const date = new Date(dateString);

    const monthNames = [
      "January", "February", "March",
      "April", "May", "June", "July",
      "August", "September", "October",
      "November", "December"
    ];
  
    const monthIndex = date.getMonth();
    const year = date.getFullYear();
  
    return date.getDate() + ' ' +  monthNames[monthIndex] + ' ' + year;
}

export function formatDate(dateString) {
    const date = new Date(dateString);
    const month = date.getMonth()+1;
    const year = date.getFullYear();

    return date.getDate() + '/' +month + '/' + year;
}

export function reverseDate(dateString) {
    const date = dateString.split("/");
    return date[2] + '-' + date[1] + '-' + date[0];
}
  
export function formatDateTime(dateTimeString) {
  const date = new Date(dateTimeString);

  const monthNames = [
    "Jan", "Feb", "Mar", "Apr",
    "May", "Jun", "Jul", "Aug", 
    "Sep", "Oct", "Nov", "Dec"
  ];

  const monthIndex = date.getMonth();
  const year = date.getFullYear();

  return date.getDate() + ' ' + monthNames[monthIndex] + ' ' + year + ' - ' + date.getHours() + ':' + date.getMinutes();
}

export function humanize(key) {
    key =  key.replace(/_/g, ' ').replace(/(?: |\b)(\w)/g, function(key) { return key.toLowerCase()});
    return key.toLowerCase().split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ');
}